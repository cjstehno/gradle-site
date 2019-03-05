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

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * The "updateVersion" task is used to update the project version string in configured "versioned files", the README.md by default. The "from" build
 * property is used to specify the old version, while the version configured in the build.gradle file is used as the new version. The old version
 * string will be replaced with the new version string in the configured files.
 */
class UpdateVersionTask extends DefaultTask {

    static final String NAME = 'updateVersion'

    @TaskAction @SuppressWarnings('GroovyUnusedDeclaration') void updateVersion() {
        SiteExtension extension = project.extensions.findByType(SiteExtension)

        String newVersion = project.version
        String oldVersion = project.property('from')

        List<String> versionedFiles = ['README.md'] + extension.versionedFiles

        if (project.file('src/site/index.html').exists()) {
            versionedFiles << 'src/site/index.html'
        }

        logger.lifecycle "Updating documentation versions from ${oldVersion} to ${newVersion}..."

        versionedFiles.findAll { p -> project.file(p).exists() }.each { f ->
            ant.replace(file: f, token: oldVersion, value: newVersion)
        }
    }
}
