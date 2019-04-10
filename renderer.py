import logging
from jinja2 import Environment
from utils import write_template_to_file

logger = logging.getLogger("spark_generator")


def render_code_files(env: Environment, language: str, feature, logger_needed: bool, package_name: str):
    """
    Generate code files according to language and features needed
    :param env:
    :param language:
    :param feature:
    :param logger_needed:
    :param package_name:
    :return:
    """
    # TODO : Add other languages here to Handle main files here
    write_template_to_file(
        env.get_template("code/" + language + "/App." + language)
           .render(language=language, feature=feature, logger=logger_needed, package_name=package_name),
        "target/App." + language)

    # TODO : Add other languages here to Handle code files
    write_template_to_file(
            env.get_template("code/" + language + "/Treatment." + language)
               .render(language=language, feature=feature, logger=logger_needed, package_name=package_name),
            "target/Treatment." + language)

    # TODO : Add other languages here to handle config file
    write_template_to_file(
            env.get_template("code/" + language + "/AppConfig." + language)
               .render(feature=feature, package_name=package_name),
            "target/AppConfig." + language)

    logger.debug("Generated code files for language : %s with feature : %s", language, feature)


def render_compiler_files(env: Environment, compiler: str, version: str, feature, project_name: str, language: str,
                          package_name: str):
    """
    Generate files needed by compiler such as pom.xml or build.sbt
    :param env:
    :param compiler:
    :param version:
    :param feature:
    :param project_name:
    :param language:
    :param package_name:
    :return:
    """
    if compiler == "maven":
        write_template_to_file(
            env.get_template("compiler/maven/pom.xml")
               .render(version=version, feature=feature, project_name=project_name, language=language,
                       package_name=package_name),
            "target/pom.xml")
    elif compiler == "sbt":
        write_template_to_file(
            env.get_template("compiler/sbt/build.sbt")
            .render(version=version, feature=feature, project_name=project_name),
            "target/build.sbt"
        )
        write_template_to_file(
            env.get_template("compiler/sbt/assembly.sbt")
               .render(),
            "target/assembly.sbt"
        )
        write_template_to_file(
            env.get_template("compiler/sbt/build.properties")
               .render(),
            "target/build.properties"
        )
        write_template_to_file(
            env.get_template("compiler/sbt/plugins.sbt")
               .render(),
            "target/plugins.sbt"
        )

    logger.debug("Generated compiler files for compiler : %s with version : %s " +
                 "and feature : %s", compiler, version, feature)


def render_script_files(env: Environment, language: str, master: str, feature, kerberos: bool, project_name: str,
                        logger_needed: bool, package_name: str, compiler: str, principal: str, keytab: str,
                        host: str, user: str):
    """
    Generate script files to deploy spark project (hence spark-submit.sh and other if needed)
    :param env:
    :param language:
    :param master:
    :param feature:
    :param kerberos:
    :param project_name:
    :param logger_needed:
    :param package_name:
    :param compiler:
    :param principal:
    :param keytab:
    :param host:
    :param user:
    :return:
    """
    if language == "scala" or language == "java":
        write_template_to_file(
            env.get_template("scripts/spark-submit.sh")
               .render(language=language, master=master, project_name=project_name, kerberos=kerberos,
                       logger=logger_needed, package_name=package_name, principal=principal, keytab=keytab),
            "target/spark-submit.sh")

        write_template_to_file(
            env.get_template("scripts/copy_to_cluster.sh")
               .render(language=language, project_name=project_name, logger=logger_needed, compiler=compiler,
                       host=host, user=user),
            "target/copy_to_cluster.sh")

    logger.debug("Generated script files for language : %s with master : %s" +
                 ", feature : %s, kerberos : %s, logger : %s", language, master, feature, kerberos, logger_needed)


def render_configuration_files(env: Environment, language: str, feature, project_name: str, master: str,
                               logger_needed: bool):
    """
    Generate configuration file (for logging and external variables)
    :param env:
    :param language:
    :param feature:
    :param project_name:
    :param master:
    :param logger_needed:
    :return:
    """
    if logger_needed:
        # TODO : Add other languages here
        if language == "scala" or language == "java":
            write_template_to_file(
                env.get_template("configuration/log4j.properties")
                   .render(project_name=project_name),
                "target/log4j.properties")

    # TODO : Add other features here
    # TODO : Add more external variables here (taken from command line arguments)
    write_template_to_file(
            env.get_template("configuration/application.conf")
               .render(feature=feature, project_name=project_name, master=master),
            "target/application.conf")

    logger.debug("Generated configuration files for language : %s", language)


def render_doc_files(env: Environment, language: str, master: str, feature, compiler: str, version: str, kerberos: bool,
                     project_name: str, doc_type: str):
    """
    Generate a doc file as a README.md or README.adoc file for the project
    :param env:
    :param language:
    :param master:
    :param feature:
    :param compiler:
    :param version:
    :param kerberos:
    :param project_name:
    :param doc_type:
    :return:
    """
    write_template_to_file(
        env.get_template("docs/README." + doc_type)
           .render(language=language, master=master, feature=feature, compiler=compiler, kerberos=kerberos,
                   version=version, project_name=project_name),
        "target/README." + doc_type)

    logger.debug("Generated doc files for language : %s with master : %s" +
                 ", feature : %s, compiler : %s, version : %s and kerberos : %s, and project_name : %s",
                 language, master, feature, compiler, version, kerberos, project_name)
