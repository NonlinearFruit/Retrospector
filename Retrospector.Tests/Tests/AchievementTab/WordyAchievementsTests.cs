using System;
using System.Linq;
using FontAwesome.Sharp;
using Retrospector.AchievementTab;
using Retrospector.AchievementTab.Interfaces;
using Retrospector.DataStorage.Models;
using Retrospector.Tests.Utilities;
using Xunit;

namespace Retrospector.Tests.Tests.AchievementTab
{
    public class WordyAchievementsTests : DatabaseContextTests
    {
        private const int Threshold = 1000;
        private readonly IAchievementGenerator _generator;

        protected WordyAchievementsTests()
        {
            _generator = new WordyAchievements(_actContext);
        }

        public class GetAchievements : WordyAchievementsTests
        {
            [Fact]
            public void has_proper_number_of_achievements()
            {
                var achievements = _generator.GetAchievements();

                Assert.Equal(3, achievements.Count);
            }

            [Fact]
            public void share_the_same_symbol()
            {
                var achievements = _generator.GetAchievements();

                var icon = Assert.Single(achievements.Select(a => a.FontAwesomeSymbol).Distinct());
                Assert.Equal(IconChar.Comment, icon);
            }

            [Fact]
            public void share_the_same_hint()
            {
                var achievements = _generator.GetAchievements();

                var hint = Assert.Single(achievements.Select(a => a.Hint).Distinct());
                Assert.Equal("But what do you really think?", hint);
            }

            [Theory]
            [InlineData("Wordy")]
            [InlineData("Thoughtful")]
            [InlineData("Scholarly")]
            public void one_has_the_title(string title)
            {
                var achievements = _generator.GetAchievements();

                Assert.Contains(title, achievements.Select(a => a.Title));
            }

            [Theory]
            [InlineData(Achievement.Gold)]
            [InlineData(Achievement.Silver)]
            [InlineData(Achievement.Bronze)]
            public void one_has_the_color(string color)
            {
                var achievements = _generator.GetAchievements();

                Assert.Contains(color, achievements.Select(a => a.Color));
            }

            [Fact]
            public void colors_are_ordered()
            {
                var achievements = _generator.GetAchievements();

                var colors = achievements.Select(a => a.Color).ToList();
                var goldIndex = colors.IndexOf(Achievement.Gold);
                var silverIndex = colors.IndexOf(Achievement.Silver);
                var bronzeIndex = colors.IndexOf(Achievement.Bronze);
                Assert.True(goldIndex < silverIndex);
                Assert.True(silverIndex < bronzeIndex);
            }

            [Theory]
            [InlineData("Give a loooong review")]
            [InlineData("Give 10 looong reviews")]
            [InlineData("Give 100 looong reviews")]
            public void one_has_the_description(string description)
            {
                var achievements = _generator.GetAchievements();

                Assert.Contains(description, achievements.Select(a => a.Description));
            }

            [Theory]
            [InlineData(0, 0, 0, 0)]
            [InlineData(1, 100, 10, 1)]
            [InlineData(100, 100, 100, 100)]
            public void progress_is_calculated_correctly(int countOfReviews, params int[] expectedProgresses)
            {
                _assertContext.Reviews.AddRange(countOfReviews, i => new Review
                {
                    Content = string.Join("", Enumerable.Repeat("A", Threshold))
                });
                _assertContext.SaveChanges();

                var achievements = _generator.GetAchievements();

                var progresses = achievements.Select(a => a.Progress).ToList();
                foreach (var expectedProgress in expectedProgresses)
                    Assert.True(progresses.Remove(expectedProgress), $"No {expectedProgress} in {string.Join(", ", progresses)}");
            }

            [Fact]
            public void ignores_short_reviews()
            {
                var review = new Review
                {
                    Content = string.Join("", Enumerable.Repeat("A", Threshold-1))
                };
                _assertContext.Reviews.Add(review);
                _assertContext.SaveChanges();

                var achievements = _generator.GetAchievements();

                Assert.True(achievements.Select(a => a.Progress).All(p => p == 0));
            }
        }
    }
}