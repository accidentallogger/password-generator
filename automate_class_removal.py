import os

def delete_files_with_extension(directory, extension):
    for filename in os.listdir(directory):
        if filename.endswith(extension):
            file_path = os.path.join(directory, filename)
            os.remove(file_path)
            print(f"Deleted {filename}")

directory_path = "E:\projects\workspace for projects\pp lab project"
file_extension = ".class"  
delete_files_with_extension(directory_path, file_extension)
