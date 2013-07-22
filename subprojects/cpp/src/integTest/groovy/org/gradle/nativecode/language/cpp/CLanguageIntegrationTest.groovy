/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.nativecode.language.cpp

class CLanguageIntegrationTest extends AbstractLanguageIntegrationTest {
    static final HELLO_WORLD = "Hello, World!"
    static final HELLO_WORLD_FRENCH = "Bonjour, Monde!"

    def helloWorldApp = new CHelloWorldApp()
    def "build fails when compilation fails"() {
        given:
        buildFile << """
             apply plugin: "cpp"
             sources {
                 main {}
             }
             executables {
                 main {
                     source sources.main
                 }
             }
         """

        and:
        file("src/main/c/broken.c") << """
        #include <stdio.h>

        'broken
"""

        expect:
        fails "mainExecutable"
        failure.assertHasDescription("Execution failed for task ':compileMainExecutableMainC'.");
        failure.assertHasCause("C compile failed; see the error output for details.")
    }

    class CHelloWorldApp {
        def englishOutput= "$HELLO_WORLD 12"
        def frenchOutput = "$HELLO_WORLD_FRENCH 12"
        def customArgs = ""


        def appSources = [
            "c/main.c": """
                #include <stdio.h>
                #include "hello.h"

                int main () {
                    sayHello();
                    printf(" %d", sum(5, 7));
                    return 0;
                }
    """
        ]

        def libraryHeaders = [
            "headers/hello.h": """
                #ifdef _WIN32
                #define DLL_FUNC __declspec(dllexport)
                #else
                #define DLL_FUNC
                #endif

                void sayHello();
                int sum(int a, int b);
    """
        ]

        def librarySources = [
            "c/hello.c": """
                #include <stdio.h>
                #include "hello.h"

                void DLL_FUNC sayHello() {
                    #ifdef FRENCH
                    printf("${HELLO_WORLD_FRENCH}");
                    #else
                    printf("${HELLO_WORLD}");
                    #endif
                }
    """,
            "c/sum.c": """
                #include "hello.h"

                int DLL_FUNC sum(int a, int b) {
                    return a + b;
                }
    """
        ]

    }
}

