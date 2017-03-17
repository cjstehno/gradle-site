/*
 * Copyright (C) 2017 Christopher J. Stehno <chris@stehno.com>
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
package com.stehno.gradle.docs

import groovy.transform.TypeChecked
import org.gradle.api.Plugin
import org.gradle.api.Project

import static groovy.transform.TypeCheckingMode.SKIP

@TypeChecked
class ProjectDocsPlugin implements Plugin<Project> {

    private static final String WEB_PREVIEW_PLUGIN_ID = 'com.stehno.gradle.webpreview'
    private static final String LICENSE_PLUGIN_ID = 'com.github.hierynomus.license'

    @Override
    void apply(final Project project) {
        // TODO: this should be configurable
        String siteBuildDir = 'build/site'

        applyWebPreviewConfig project, siteBuildDir
        applyLicenseConfig project

        // TODO: depends on: build, jacocoTestReport (others?)
        project.task SiteTask.NAME, type: SiteTask, group: 'Documentation', description: 'Generates the project web site.'

        project.task CheckVersionTask.NAME, type: CheckVersionTask, group: 'Documentation', description: 'Verify that the project version is reflected in the documentation.'
        project.task UpdateVersionTask.NAME, type: UpdateVersionTask, group: 'Documentation', description: 'Updates the version in documentation based on the project version'
        project.task PublishSiteTask.NAME, type: PublishSiteTask, group: 'Documentation', description: 'Publishes the documentation web site.'
        project.task VerifySiteTask.NAME, type: VerifySiteTask, group: 'Documentation', description: 'Verifies that the documentation site exists at the expected address.'
    }

//    FIXME: test the two applied configurations

    @TypeChecked(SKIP)
    private static void applyWebPreviewConfig(final Project project, final String siteBuildDir) {
        // if the web preview plugin is configured, apply the default config

        project.plugins.withId(WEB_PREVIEW_PLUGIN_ID) {
            Object webPreviewExt = project.extensions.findByName('webPreview')
            webPreviewExt.resourceDir = project.file(siteBuildDir)
        }

        project.logger.info 'Applied default configuration for Web Preview plugin.'
    }

    @TypeChecked(SKIP)
    private static void applyLicenseConfig(final Project project){
        project.plugins.withId(LICENSE_PLUGIN_ID){
            // TODO: these files should be configurable
            if( !project.file('license_header.txt').exists() ){
                project.logger.lifecycle 'No license_header.txt file found, creating one.'

                project.file('license_header.txt').text = ProjectDocsPlugin.getResource('/license_header.template').text
            }

            if( !project.file('LICENSE').exists() ){
                project.logger.lifecycle 'No LICENSE file found, creating one.'

                project.file('LICENSE').text = ProjectDocsPlugin.getResource('/LICENSE.template').text
            }

            // TODO: make this configurable
            def owner = [name:'Christopher J. Stehno', email:'chris@stehno.com']

            // TODO: apply the license config
            Object licenseExt = project.extensions.findByName('license')
            licenseExt.with {
                header project.file('license_header.txt')
                ext.name = owner.name
                ext.email = owner.email
                ext.year = Calendar.instance.get(Calendar.YEAR)
            }
        }
    }
}