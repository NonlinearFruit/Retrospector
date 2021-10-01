using System.Collections.Generic;
using System.Linq;
using FontAwesome.Sharp;
using Retrospector.AchievementTab.Interfaces;
using Retrospector.DataStorage.Interfaces;

namespace Retrospector.AchievementTab
{
    public class WordyAchievements : IAchievementGenerator
    {
        private readonly IDatabaseContext _context;

        public WordyAchievements(IDatabaseContext context)
        {
            _context = context;
        }

        public ICollection<Achievement> GetAchievements()
        {
            var threshold = 1000;
            var hint = "But what do you really think?";
            var (gold, silver, bronze) = (100, 10, 1);
            var numberOfLongReviews = _context.Reviews.Count(r => r.Content.Length >= threshold);
            return new List<Achievement>
            {
                new () {
                    Title = "Scholarly",
                    Color = Achievement.Gold,
                    Description = $"Give {gold} looong reviews",
                    Hint = hint,
                    FontAwesomeSymbol = IconChar.Comment,
                    Progress = Scale.ToPercent(numberOfLongReviews, gold)
                },
                new () {
                    Title = "Thoughtful",
                    Color = Achievement.Silver,
                    Description = $"Give {silver} looong reviews",
                    Hint = hint,
                    FontAwesomeSymbol = IconChar.Comment,
                    Progress = Scale.ToPercent(numberOfLongReviews, silver)
                },
                new () {
                    Title = "Wordy",
                    Color = Achievement.Bronze,
                    Description = "Give a loooong review",
                    Hint = hint,
                    FontAwesomeSymbol = IconChar.Comment,
                    Progress = Scale.ToPercent(numberOfLongReviews, bronze)
                }
            };
        }
    }
}