using System;

namespace Retrospector.AchievementTab
{
    public static class Scale
    {
        public static int ToPercent(int value, int max)
        {
            return Math.Min(value*100/max, 100);
        }
    }
}