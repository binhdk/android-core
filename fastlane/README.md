fastlane documentation
----

# Installation

Make sure you have the latest version of the Xcode command line tools installed:

```sh
xcode-select --install
```

For _fastlane_ installation instructions, see [Installing _fastlane_](https://docs.fastlane.tools/#installing-fastlane)

# Available Actions

## Android

### android build_dev

```sh
[bundle exec] fastlane android build_dev
```

Build dev

### android build_staging

```sh
[bundle exec] fastlane android build_staging
```

Build staging

### android build_production

```sh
[bundle exec] fastlane android build_production
```

Build production

### android build_aab

```sh
[bundle exec] fastlane android build_aab
```

Build aab

### android distribute_dev

```sh
[bundle exec] fastlane android distribute_dev
```

Distribute dev app to Firebase

### android distribute_staging

```sh
[bundle exec] fastlane android distribute_staging
```

Distribute staging app to Firebase

### android distribute

```sh
[bundle exec] fastlane android distribute
```

Distribute production app to Firebase

----

This README.md is auto-generated and will be re-generated every time [_fastlane_](https://fastlane.tools) is run.

More information about _fastlane_ can be found on [fastlane.tools](https://fastlane.tools).

The documentation of _fastlane_ can be found on [docs.fastlane.tools](https://docs.fastlane.tools).
