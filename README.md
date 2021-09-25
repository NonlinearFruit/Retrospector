# Retrospector
[![GitHub Workflow Status](https://img.shields.io/github/workflow/status/NonlinearFruit/Retrospector/.NET%20Core)](https://github.com/NonlinearFruit/Retrospector/actions/workflows/dotnet-core.yml)
[![Code Coverage](https://img.shields.io/codecov/c/gh/NonlinearFruit/Retrospector.svg)](https://codecov.io/gh/NonlinearFruit/Retrospector)
[![GitHub tag (latest SemVer)](https://img.shields.io/github/v/tag/NonlinearFruit/Retrospector)](https://gitlab.com/NonlinearFruit/retrospector/-/tags)
[![license](https://img.shields.io/badge/license-unlicense-yellow.svg)](https://github.com/NonlinearFruit/Retrospector/blob/master/LICENSE)

> **UPDATE:** We are in the process of rewriting this repo. First to .Net with WPF, then to .Net Maui (when Windows support is available).

Maintain an active list of your personal reviews/ratings of media. In a nutshell, with Retrospector you create a *Media* by entering a title and creator. Then you review that media by entering a number (1-10). In a month or a year, when you are looking for a movie to watch with friends, you can have your friends pick from your top 10 movies.

There are a lot of practical use cases. Once you start using Retrospector, you will wonder how you lived without being able to answer questions like 'What was my average rating for Firefly?' or 'How many times have I seen *The Holy Grail*?'

Instructions on how to install Retrospector are [here](https://github.com/NonlinearFruit/Retrospector/wiki/Install). In short, you make sure you have Java (a recent version), download the `Retrospector.zip` from the [latest release](https://github.com/NonlinearFruit/Retrospector/releases/latest), unzip and run `Retrospector.jar`.

# Features
There are a variety of features baked into Retrospector. Here are the most basic, to help get you started.

## Search
Looking up media is a big part of using Retrospector. There are a lot of search commands. The most basic of which is the `:` which acts as an `and` operator.
![screenshot](/screenshots/Search.gif)

## Media
This is where media happens.
![screenshot](/screenshots/Media.gif)

## Review
Entering a review is pretty simple. Select a number and click save. The user will default to the name in the config file and the date will automatically be today (both are editable). The text area can also be used for entering thoughts or comments or critiques.
![screenshot](/screenshots/Review.gif)

## Chart
The best part of storing reviews is seeing charts!
![screenshot](/screenshots/Chart.gif)

## List
'Top Ten' lists are really useful. Especially when making recommendations to friends.
![screenshot](/screenshots/List.gif)

## Achievements
There are several achievements that you can earn by using Retrospector to its fullest potential. [Warning: these are addictive]
![screenshot](/screenshots/Achievements.gif)

## Migrations

 - Generate with `dotnet ef migrations add --project Retrospector/Retrospector.csproj -o DataStorage/Migrations <name>`