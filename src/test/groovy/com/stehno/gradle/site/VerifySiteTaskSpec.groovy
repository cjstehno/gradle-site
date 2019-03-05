/*
 * Copyright (C) 2019 Christopher J. Stehno <chris@stehno.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stehno.gradle.site

import com.stehno.ersatz.ErsatzServer
import org.gradle.testkit.runner.BuildResult
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.AutoCleanup
import spock.lang.Specification

class VerifySiteTaskSpec extends Specification implements UsesGradleBuild {

    @Rule TemporaryFolder projectRoot = new TemporaryFolder()

    @AutoCleanup('stop') ErsatzServer ersatzServer = new ErsatzServer({
        autoStart()
    })

    final String buildTemplate = '''
        plugins {
            id 'com.stehno.gradle.site'
        }
        
        version = '1.2.3'
        
        repositories {
            jcenter()
        }
        ${config.extension ?: ''}
    '''

    def 'verifySite (succeeds)'() {
        setup:
        buildFile(extension: '')

        ersatzServer.expectations {
            head('/index.html').responds().code(200)
        }

        when: 'the verifySite task is run'
        BuildResult result = gradleRunner("verifySite -PsiteUrl=${ersatzServer.httpUrl}").build()

        then: 'the build is successful'
        totalSuccess result
    }

    def 'verifySite configured (succeeds)'() {
        setup:
        ersatzServer.expectations {
            head('/index.html').responds().code(200)
            head('/foo.html').responds().code(200)
        }

        buildFile(extension: """
            site {
                siteUrl = '${ersatzServer.httpUrl}' 
                testedPath '/foo.html'
            }
        """)

        when: 'the verifySite task is run'
        BuildResult result = gradleRunner("verifySite").build()

        then: 'the build is successful'
        totalSuccess result
    }

    def 'verifySite (fails)'() {
        setup:
        buildFile(extension: '')

        ersatzServer.expectations {
            head('/index.html').responds().code(404)
        }

        when: 'the verifySite task is run'
        gradleRunner("verifySite -PsiteUrl=${ersatzServer.httpUrl}").build()

        then: 'the build fails'
        def ex = thrown(Exception)
        ex.message.contains('Execution failed for task \':verifySite\'.')
    }
}
