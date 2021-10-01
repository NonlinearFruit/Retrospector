using System.Collections.Generic;
using System.Linq;
using FontAwesome.Sharp;
using Retrospector.AchievementTab.Interfaces;
using Retrospector.DataStorage.Interfaces;

namespace Retrospector.AchievementTab
{
    public class SocialAchievements : IAchievementGenerator
    {
        private readonly IDatabaseContext _context;

        public SocialAchievements(IDatabaseContext context)
        {
            _context = context;
        }

        public ICollection<Achievement> GetAchievements()
        {
            var (gold, silver, bronze) = (10, 6, 3);
            var userCount = _context.Reviews.Select(r => r.User).Distinct().Count();
            return new List<Achievement>
            {
                new()
                {
                    Title = "Gregarious",
                    Color = "Gold",
                    Description = $"Have {gold} users",
                    Hint = "Retrospecting is a great way to make friends*",
                    FontAwesomeSymbol = IconChar.Users,
                    Progress = Scale.ToPercent(userCount, gold)
                },
                new()
                {
                    Title = "Amicable",
                    Color = "Silver",
                    Description = $"Have {silver} users",
                    Hint = "Retrospecting is a great way to make friends*",
                    FontAwesomeSymbol = IconChar.Users,
                    Progress = Scale.ToPercent(userCount, silver)
                },
                new()
                {
                    Title = "Social",
                    Color = "SaddleBrown",
                    Description = $"Have {bronze} users",
                    Hint = "Retrospecting is a great way to make friends*",
                    FontAwesomeSymbol = IconChar.Users,
                    Progress = Scale.ToPercent(userCount, bronze)
                }
            };
        }
    }
}