import shutil
import sys
from pathlib import Path

ROOT = Path("src/main/java/br/upe/reservapatterns")
AREAS = ("kit/creation", "booking/creation", "handover/creation")


def main():
    submission, official = (Path(argument) for argument in sys.argv[1:3])
    count = 0
    for area in AREAS:
        source = submission / ROOT / area
        target = official / ROOT / area
        if not source.exists():
            continue
        if source.is_symlink() or not source.is_dir():
            raise ValueError(f"Diretório inválido: {area}")
        for candidate in source.rglob("*.java"):
            relative = candidate.relative_to(source)
            if (candidate.is_symlink() or not candidate.is_file()
                    or any(parent.is_symlink() for parent in candidate.parents if parent != source
                           and source in parent.parents)):
                raise ValueError(f"Arquivo inválido: {relative}")
            if candidate.stat().st_size > 65536:
                raise ValueError(f"Arquivo maior que 64 KiB: {relative}")
            destination = target / relative
            destination.parent.mkdir(parents=True, exist_ok=True)
            shutil.copyfile(candidate, destination)
            count += 1
            if count > 30:
                raise ValueError("Mais de 30 classes de exercício")
    print(f"{count} fontes Java dos exercícios copiadas")


if __name__ == "__main__":
    main()
