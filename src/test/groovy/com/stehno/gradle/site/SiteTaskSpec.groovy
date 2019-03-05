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

import org.gradle.testkit.runner.BuildResult
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class SiteTaskSpec extends Specification implements UsesGradleBuild {

    // FIXME: test reports and docs

    @Rule TemporaryFolder projectRoot = new TemporaryFolder()

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

    def 'site (no site source)'() {
        setup:
        buildFile(extension: '')

        when: 'the site task is run'
        BuildResult result = gradleRunner('site').build()

        then: 'the build is successful'
        totalSuccess result

        and: 'no content is generated'
        fileOmitted 'build/site'
    }

    def 'site (simple)'() {
        setup:
        buildFile(extension: '')

        fileTree().dir('src') {
            dir('site') {
                file('index.html', 'Content for version ${project_version} in ${year}')
            }
        }

        when: 'the site task is run'
        BuildResult result = gradleRunner('site').build()

        then: 'the build is successful'
        totalSuccess result

        and: 'the expected content is generated'
        fileExists 'build/site/index.html', "Content for version 1.2.3 in ${Calendar.instance.get(Calendar.YEAR)}"
    }

    def 'site (complex)'() {
        setup:
        buildFile(extension: '')

        fileTree().dir('src') {
            dir('site') {
                dir('css') {
                    file('index.css', 'This is CSS. ${year}')
                }
                dir('js') {
                    file('main.js', 'This is JavaScript. ${year}')
                }
                dir('img') {
                    file('icon.png', 'This is an image. ${year}')
                }
                dir('foo') {
                    file('todo.md', 'This is non-site content. ${year}')
                }
                dir('other') {
                    file('info.html', 'This is more content ${project_version} in ${year}')
                }
                file('index.html', 'Content for version ${project_version} in ${year}')
            }
        }

        when: 'the site task is run'
        BuildResult result = gradleRunner('site').build()

        then: 'the build is successful'
        totalSuccess result

        and: 'the expected content is generated'
        fileExists 'build/site/css/index.css', 'This is CSS. ${year}'
        fileExists 'build/site/js/main.js', 'This is JavaScript. ${year}'
        fileExists 'build/site/img/icon.png', 'This is an image. ${year}'
        fileOmitted 'build/site/foo/todo.md'
        fileExists 'build/site/index.html', "Content for version 1.2.3 in ${Calendar.instance.get(Calendar.YEAR)}"
        fileExists 'build/site/other/info.html', "This is more content 1.2.3 in ${Calendar.instance.get(Calendar.YEAR)}"
    }
}
