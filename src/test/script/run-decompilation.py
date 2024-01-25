import subprocess
import filecmp
import os

def delete_files(directory, extension):
    print(f"Début de la suppression des fichiers avec l'extension {extension} dans le dossier {directory}")
    files = [f for f in os.listdir(directory) if os.path.isfile(os.path.join(directory, f)) and f.endswith(extension)]
    
    for file_name in files:
        file_path = os.path.join(directory, file_name)
        os.remove(file_path)

    print(f"Fin de la suppression des fichiers avec l'extension {extension} dans le dossier {directory}")

def execute_decac(input_file):
    # Exécute decac -p sur le fichier d'entrée et renvoie le résultat
    result = subprocess.run(['decac', '-p', input_file], capture_output=True, text=True)
    return result.stdout

def compare_files(file1, file2):
    # Compare deux fichiers et renvoie True s'ils sont identiques, False sinon
    return filecmp.cmp(file1, file2)

def main():
    for folder_path in ['../deca/context/valid/', '../deca/codegen/valid/']:
        print(f"Test de la stabilité de decac -p dans le dossier {folder_path}")
        files = [f for f in os.listdir(folder_path) if os.path.isfile(os.path.join(folder_path, f)) and f.endswith(".deca")]

        for input_file in files:
            # Exécute decac -p sur le fichier d'entrée
            output_file = f"{os.path.splitext(input_file)[0]}.pt"
            result = execute_decac(os.path.join(folder_path, input_file))

            # Écrit le résultat dans le fichier de sortie
            with open(os.path.join(folder_path, output_file), 'w') as f:
                f.write(result)

            # Exécute decac -p sur le fichier de sortie
            final_output_file = f"{output_file}.pt"
            final_result = execute_decac(os.path.join(folder_path, output_file))

            with open(os.path.join(folder_path, final_output_file), 'w') as f:
                f.write(result)

            # Vérifie si les deux fichiers sont identiques
            if not compare_files(os.path.join(folder_path, output_file), os.path.join(folder_path, final_output_file)):
                print(f"[WARNING] decac -p non stable pour {output_file}")
            else:
                print(f"[OK] {output_file}")

        # Supprime les fichiers .pt dans le dossier actuel
        delete_files(folder_path, ".pt")

if __name__ == "__main__":
    main()

