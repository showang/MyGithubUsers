# MyGithubUsers

## Architecture

### User List Page
**I write the user list page by using _Stateful MVVM_ pattern.**

The Stateful MVVM is an architecture which I designed by finite-state machine to describe the UI State and solving some lifecycle issues when using Fragment.

The most important thing is include the data flow concepts to MVVM architecture.

>#### View States
>* Initializing
>* DataLoaded
>* ErrorState

### User Detail Page
**I use _MVP pattern_ to design this page.**

Just showing the skill which i have and this pattern is more easily for some ui in simple design.

## Unit Tests

**I build some unit tests for view model, presenter and the transactions of view state .**

## CI

**Using Github Actions to run the CI**

## 3rd Party Libraries created by myself
* [Recyct](https://github.com/showang/Recyct): A powerful recycler view library.
* [Respect](https://github.com/showang/Respect): A simple RESTful API framework based on OkHttp3.
