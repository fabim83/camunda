package de.adesso.camunda;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CamundaApplication {

    /** This class' logger. */
    private static final Logger LOG = LoggerFactory.getLogger(CamundaApplication.class);

    public static void main(String[] args) {
	SpringApplication.run(CamundaApplication.class, args);

	LOG.info("Camunda-Application started");
	for (String line : BANNER_HASE) {
	    LOG.info(line);
	}
    }

    // @formatter:off
    private static final String[] BANNER_HASE = new String[] {
        "************************************************************************************",
        "                    ____     ____",
        "                  /'    |   |    \\ ",
        "                /    /  |   | \\   \\ ",
        "              /    / |  |   |  \\   \\ ",
        "             :   /   |  \"\"\"\"   |\\   \\ ",
        "             | /   / /^\\    /^\\  \\  _|",
        "              ~   | |   |  |   | | ~",
        "                  | |__O|__|O__| |",
        "                /~~      \\/     ~~\\       Camunda started... powered by project turbo",
        "               /   (      |      )  \\ ",
        "         _--_  /,   `\\___/^\\___/'   \\  _--_",
        "       /~    ~\\ / -____-|_|_|-____-\\ /~    ~\\",
        "     /____--------/~~~~\\---/~~~~\\ -----------_____",
        "--~~~          ^ |     |   |     |  -     :       ~:~-_     ___-----~~~~~~~~|",
        "   /             `^-^-^'   `^-^-^'                  :  ~\\ /'   ____/--------|",
        "       --                                            ;   | /~~~------~~~~~~~~~|",
        " ;                                    :              :   | ----------/--------|",
        ":                     ,                           ;   .  |---\\--------------|",
        " :     -                          .                  : : |______________-__|",
        "  :              ,                 ,                :   /'~----___________|",
        "__  \\\\        ^                          ,, ;; ;; ;._-~",
        "  ~~~-----____________________________________----~~~",
        "",
        "************************************************************************************"
    };
     // @formatter:on

}
