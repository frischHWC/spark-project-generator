import logging
import datetime
from jinja2 import Environment, select_autoescape, FileSystemLoader
from renderer import render_code_files, render_compiler_files, render_configuration_files, render_doc_files, \
    render_script_files
from orderer import order_files
from utils import clean_target, check_files_and_compilation
from commandline import command_line_arguments_to_dict, check_command_lines


def main():

    start_time = datetime.datetime.now().timestamp()

    logger.info("Start to generate Spark project files")

    clean_target()

    render_code_files(env, dict_of_options.get("language"), dict_of_options.get("feature"),
                      dict_of_options.get("logger"), dict_of_options.get("packageName"))

    render_compiler_files(env, dict_of_options.get("compiler"), dict_of_options.get("version"),
                          dict_of_options.get("feature"), dict_of_options.get("projectName"),
                          dict_of_options.get("language"), dict_of_options.get("packageName"))

    render_script_files(env, dict_of_options.get("language"), dict_of_options.get("master"),
                        dict_of_options.get("feature"), dict_of_options.get("kerberos"),
                        dict_of_options.get("projectName"), dict_of_options.get("logger"),
                        dict_of_options.get("packageName"), dict_of_options.get("compiler"),
                        dict_of_options.get("principal"), dict_of_options.get("keytab"),
                        dict_of_options.get("host"), dict_of_options.get("user"))

    render_configuration_files(env, dict_of_options.get("language"), dict_of_options.get("feature"),
                               dict_of_options.get("projectName"), dict_of_options.get("master"),
                               dict_of_options.get("logger"))

    render_doc_files(env, dict_of_options.get("language"), dict_of_options.get("master"),
                     dict_of_options.get("feature"), dict_of_options.get("compiler"),
                     dict_of_options.get("version"), dict_of_options.get("kerberos"),
                     dict_of_options.get("projectName"), dict_of_options.get("docFiles"))

    logger.info("Finished to generate Spark project files")

    order_files(dict_of_options.get("language"), dict_of_options.get("compiler"),
                dict_of_options.get("projectName"), dict_of_options.get("logger"),
                dict_of_options.get("packageName"))

    check_files_and_compilation(dict_of_options.get("language"), dict_of_options.get("compiler"),
                                dict_of_options.get("projectName"), dict_of_options.get("compilation"),
                                dict_of_options.get("sendFiles"))

    time_spent_in_ms = (datetime.datetime.now().timestamp()-start_time)*1000
    if time_spent_in_ms > 10*1000:
        logger.info("Execution of Spark project generator took : %.2f seconds", time_spent_in_ms/1000)
    else:
        logger.info("Execution of Spark project generator took : %.2f milliseconds", time_spent_in_ms)


if __name__ == "__main__":
    # Prepare logger
    logger = logging.getLogger("spark_generator")
    logger.setLevel(logging.DEBUG)

    formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')

    file_handler = logging.FileHandler("spark_generator.log")
    file_handler.setLevel(logging.INFO)
    file_handler.setFormatter(formatter)

    console_handler = logging.StreamHandler()
    console_handler.setLevel(logging.DEBUG)
    console_handler.setFormatter(formatter)

    logger.addHandler(file_handler)
    logger.addHandler(console_handler)

    # Get and check command line options
    dict_of_options = command_line_arguments_to_dict()
    check_command_lines(dict_of_options)

    # Load environment
    env = Environment(
        loader=FileSystemLoader('templates/'),
        autoescape=select_autoescape(['md', 'adoc', 'xml', 'conf', 'sh', 'java', 'scala', 'py'])
    )

    main()
