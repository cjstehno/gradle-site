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

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * FIXME: document
 */
class PublishSiteTask extends DefaultTask {

    // FIXME: only active when org.ajoberstar.github-pages plugin is available
    // FIXME: need to figure out how to implement this

    static final String NAME = 'publishSite'

    @TaskAction @SuppressWarnings('GroovyUnusedDeclaration') void publishSite() {

    }

}

/*
task publishSite(type: GradleBuild, group: 'Publishing', description: 'Publishes the documentation web site.', dependsOn: ['site']) {
    buildFile = 'publish.gradle'
    tasks = ['publishGhPages']
}


plugins {
    id "org.ajoberstar.github-pages" version "1.6.0"
}

githubPages {
    repoUri = 'git@github.com:cjstehno/gradle-project-docs'
    pages {
        from(file('build/site')) {
            into '.'
        }
    }
}



    @TaskAction void publish() {
        if (!project.plugins.findPlugin(GithubPagesPlugin)) {
            project.apply(plugin: 'org.ajoberstar.github-pages')
        }

        GithubPagesPluginExtension githubPages = project.extensions.findByType(GithubPagesPluginExtension)
        githubPages.repoUri = repoUri
        if (targetBranch) githubPages.targetBranch = targetBranch
        githubPages.pages {
            from(contentDir) {
                into '.'
            }
        }

        // FIXME: need to figure out how to actually run the task - this fails
        Action.execute(project.tasks.findByName('publishGhPages'))
    }
 */