# Forlago 🍷
<img src="/art/play_store_feature_graphic.png" width="300" align="right" hspace="0" />

![CI](https://github.com/leinardi/Forlago/workflows/CI/badge.svg)  ![License](https://img.shields.io/github/license/leinardi/Forlago.svg) ![Language](https://img.shields.io/github/languages/top/leinardi/Forlago?color=blue&logo=kotlin)

A Github template that lets you create an enterprise grade **Android / Compose Only / Single Activity / Multi Module** application project.

## How to use 👣

Just click on [![Use this template](https://img.shields.io/badge/-Use%20this%20template-brightgreen)](https://github.com/leinardi/Forlago/generate)
button to create a new repo starting from this template.

## Features 🎨

- 100% Kotlin-only Android code
- Compose only - Single Activity application
- Multi Module project structure
- CI Setup with GitHub Actions
- Dependency versions managed via `libs.versions.toml`


## Gradle Setup 🐘
TBD

## Static Analysis 🔍
TBD

## CI ⚙️

This template is using [**GitHub Actions**](https://github.com/leinardi/Forlago/actions) as CI. You don't need to setup any external service and you should have a running CI once you start using this template.

There are currently the following workflows available:
- [CI](.github/workflows/ci.yml) - Will be triggered on every new PR or every new commit pushed to the `master` branch.

## Contributing 🤝

Feel free to open a issue or submit a pull request for any bugs/improvements.



# WIP

adb shell am start -d "forlago://bar/FooText" -a android.intent.action.VIEW
adb shell am start -d "forlago://debug" -a android.intent.action.VIEW

Inspired by the following open source projects:

- https://github.com/FunkyMuse/Aurora
- https://github.com/dbaelz/PnPStats/
- https://github.com/catalinghita8/android-compose-mvvm-foodies
- https://proandroiddev.com/mvi-architecture-with-kotlin-flows-and-channels-d36820b2028d
- https://github.com/chrisbanes/tivi/
- https://github.com/alorma/Compose-Settings
- https://github.com/SamYStudiO/Beaver
- https://github.com/mohodroid/android-custom-account-manager
