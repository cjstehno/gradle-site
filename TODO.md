
```groovy
projectDocs {
    owner {
        name 'Christopher J. Stehno'
        email 'chris@stehno.com'
    }
    
    features {
        licensing true
        preview true
        coverage true
        quality true
        docs true
        guide true
    }
}
```

The `features` closure provides a DSL extension to allow toggling of features so that configuration may be defined in the project itself.

## `license` plugin

When the [`license` plugin](https://github.com/hierynomus/license-gradle-plugin) is present in the build:

* ensure LICENSE and license_header exist, if not nag log
* add `installLicense` task:
    * if no LICENSE file exists, install one
    * if no license_header exists, install one
* apply the license config

    license {
        header rootProject.file('license_header.txt')
        ext.name = projectDocs.owner.name
        ext.email = projectDocs.owner.email
        ext.year = Calendar.instance.get(Calendar.YEAR)
    }

## `webpreview` plugin

When the Web Preview (mine) is present in the build:

* apply the webpreview config

    webPreview {
        resourceDir = file('build/site')
    }
    
## `jacoco` plugin (also `coveralls`)

When the `jacoco` plugin is present (with optional `coveralls` existence):

* the reports will be included in the site build
* apply the config

    jacocoTestReport {
        reports {
            xml.enabled = true // if coveralls is also present
            html.enabled = true
        }
    }

## `codenarc` plugin

If the `codenarc` plugin is present:

* nag logging if the codenarc config files are not setup
* Add `installCodeNarcConfig` to install config files
* reports are included in site
* apply the codenarc config (see projects)

## `groovydoc` plugin

If the groovydoc plugin is configured:

* apply the config
* reports are included in site

## `javadoc` plugin

* apply the config (including optional asciidoc)
* reports are included in site

## `asciidoctor` plugin

* content is included in site

----

## Tasks

* `installConfig` - Downloads and/or creates the missing standard configuration files based on the configured plugins. This should generally only be run once unless plugins are added. The task should be careful to not override existing content (but log the conflict).
