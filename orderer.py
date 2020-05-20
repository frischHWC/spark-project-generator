import logging
import os
from utils import create_folder, clean_directory, copy_file, move_file

logger = logging.getLogger("spark_generator")


def order_files(language: str, compiler: str, project_name: str, logger_needed: bool, package_name: str):
    """
    Create project and all other needed folders
    Copy generated files into right folders
    :param language:
    :param compiler:
    :param project_name:
    :param logger_needed:
    :param package_name:
    :return:
    """

    folder_path = "../" + project_name + "/"
    package_path = package_name.replace(".", "/") + "/"

    # Remove older folder if it exists
    clean_directory(folder_path)

    # Create main folder
    create_folder(folder_path)

    # Create and order resources files folder
    # TODO : Add other languages here
    if language == "scala" or language == "java":
        create_folder(folder_path + "src/main/resources/")
        copy_file("target/application.conf", folder_path + "src/main/resources/application.conf")
        if logger_needed:
            copy_file("target/log4j.properties", folder_path + "src/main/resources/log4j.properties")
    elif language == "python":
        create_folder(folder_path + "resources/")

    # Create and order code files folder
    # TODO : Add other languages here
    if language == "scala" or language == "java":
        create_folder(folder_path + "src/main/" + language + "/" + package_path)
        files = [f for f in os.listdir("target/") if "." + language in f and "test" not in f]
        for file in files:
            copy_file("target/" + file, folder_path + "src/main/" + language + "/" + package_path + file)
    elif language == "python":
        create_folder(folder_path + "src/")
        files = [f for f in os.listdir("target/") if ".py" in f and "test" not in f]
        for file in files:
            copy_file("target/" + file, folder_path + file)

    # Create and order test files folder
    # TODO : Add other languages here
    if language == "scala" or language == "java":
        create_folder(folder_path + "src/test/")
        files = [f for f in os.listdir("target/") if "." + language in f and "test" in f]
        for file in files:
            copy_file("target/" + file, folder_path + "src/test/" + language + "/" + package_path + file)
    elif language == "python":
        create_folder(folder_path + "test/")
        files = [f for f in os.listdir("target/") if ".py" in f and "test" in f]
        for file in files:
            copy_file("target/" + file, folder_path + "test/" + file)

    # Put compiler, script and doc files
    files = [f for f in os.listdir("target/") if ".xml" in f or ".sbt" in f or ".sh" in f or ".md" in f
             or ".adoc" in f or ".properties" in f]
    for file in files:
        copy_file("target/" + file, folder_path + file)

    # Arrange sbt compiler files
    if compiler == "sbt":
        create_folder(folder_path + "project/")
        move_file(folder_path + "build.properties", folder_path + "project/build.properties")
        move_file(folder_path + "assembly.sbt", folder_path + "project/assembly.sbt")
        move_file(folder_path + "plugins.sbt", folder_path + "project/plugins.sbt")

    # Make .sh files executable
    sh_files = [f for f in os.listdir(folder_path) if f.endswith(".sh")]
    for file in sh_files:
        os.chmod(os.path.join(folder_path, file), 0o755)

    logger.info("Finished to order files for language : %s and compiler : %s", language, compiler)
