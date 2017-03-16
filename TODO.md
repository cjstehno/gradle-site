Check for these plugins and apply my default configs (or add tasks):

* license
* jacoco
* groovydoc
* asciidoctor
* coveralls
* webpreview

Add these tasks:

* site task
* publishSite
* updateVersion
* verifySite
* checkVersion
* release
* verifyRelease

Need to ensure that everything is configurable so that this works across all the projects.

* ability to include other dirs in site (groovydoc, coverage, asciidoc)

use the strategy of checking if the plugin is available to enable added fatures - the project will still need to apply the desired plugins but that will drive the default config

```
project {
  owner {
    name NAME
    email EMAIL
  }
  
  site {
    // how to apply extra site content dirs?
  }
  
  version {
    // additional files to update/check for version
  }
  
  release {
    
  }
}
```

Should be some means of disabling any of the provided config for cases when the project wants to use that plugin in a default manner (or configured in the project)
