import argparse
import html
import json
import os
from pathlib import Path
import xml.etree.ElementTree as ET


def read_results(path):
    grouped = {}
    for xml_file in sorted(Path(path).glob("TEST-*.xml")):
        try:
            root = ET.parse(xml_file).getroot()
        except ET.ParseError:
            continue
        for case in root.iter("testcase"):
            if case.find("skipped") is not None:
                continue
            class_name = case.get("classname") or root.get("name") or ""
            failure = case.find("failure")
            error = case.find("error")
            problem = failure if failure is not None else error
            detail = "" if problem is None else problem.get("message") or problem.get("type") or ""
            detail = html.escape(" ".join(detail.split())[:160]).replace("|", "\\|")
            grouped.setdefault(class_name, []).append(
                (case.get("name") or "teste", problem is None, detail)
            )
    return grouped


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--build-status", type=int, required=True)
    parser.add_argument("--lint-status", type=int, required=True)
    args = parser.parse_args()
    spec = json.loads(Path(".github/evaluation/requirements.json").read_text())
    cases = read_results("target/surefire-reports")
    compiled = args.build_status == 0
    security = cases.get("br.upe.reservapatterns.security.SecurityBaselineTest", [])
    secure = compiled and len(security) >= 3 and all(result[1] for result in security)
    flow = cases.get("br.upe.reservapatterns.flow.ReservationFlowTest", [])
    integrated = compiled and len(flow) >= 1 and all(result[1] for result in flow)
    rows = []
    for req in spec["requisitos"]:
        current = cases.get(req.get("classe"), [])
        if req.get("tipo") == "lint":
            ratio = int(compiled and args.lint_status == 0)
            detail = "sem violações" if ratio else "não passou"
        elif not compiled:
            ratio = 0
            detail = "não compilou"
        elif not current:
            ratio = 0
            detail = "nenhum teste executado"
        else:
            passed = sum(ok for _, ok, _ in current)
            ratio = passed / len(current)
            detail = f"{passed}/{len(current)} testes"
        if not secure:
            ratio = 0
            detail = "bloqueado: segurança base não passou"
        rows.append((req, ratio, detail, current))
    total = round(sum(req["peso"] * ratio for req, ratio, _, _ in rows), 1)
    verdict = ("APROVADO" if total >= spec["notaMinima"] and integrated
               else "AINDA NÃO APROVADO")
    lines = [
        "<!-- avaliador-reservapatterns -->",
        "## Avaliação automática — ReservaPatterns",
        "",
        f"**Nota: {total:.1f}/100 — {verdict}** (mínimo {spec['notaMinima']})",
        "",
        f"**Segurança:** {'passou' if secure else 'falhou / não executou'}; "
        f"**fluxo HTTP completo:** {'passou' if integrated else 'pendente'}. "
        "Ambos são exigidos para aprovação.",
        "",
        "| Requisito | Resultado | Peso | Nota |",
        "| --- | --- | ---: | ---: |",
    ]
    for req, ratio, detail, _ in rows:
        icon = "✅" if ratio == 1 else "🟡" if ratio else "❌"
        lines.append(
            f"| {icon} {req['titulo']} | {detail} | {req['peso']} | "
            f"{req['peso'] * ratio:.1f} |"
        )
    lines.append("")
    failed = [(req, case) for req, _, _, current in rows for case in current if not case[1]]
    if failed:
        lines.extend(["<details><summary>Testes que ainda falham</summary>", ""])
        for req, (name, _, detail) in failed:
            safe_name = html.escape(name).replace("|", "\\|").replace("`", "")
            lines.append(f"- {req['titulo']}: `{safe_name}` — {detail}"[:400])
        lines.extend(["", "</details>", ""])
    lines.append("_A nota é recalculada a cada push no PR._")
    report = "\n".join(lines) + "\n"
    Path("nota.md").write_text(report, encoding="utf-8")
    print(report)
    if os.environ.get("GITHUB_STEP_SUMMARY"):
        with open(os.environ["GITHUB_STEP_SUMMARY"], "a", encoding="utf-8") as output:
            output.write(report)
    if os.environ.get("GITHUB_OUTPUT"):
        with open(os.environ["GITHUB_OUTPUT"], "a", encoding="utf-8") as output:
            output.write(f"nota={total:.1f}\nminimo={spec['notaMinima']}\n")


if __name__ == "__main__":
    main()
