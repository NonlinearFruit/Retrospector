using Optional;
using Xunit;

namespace Retrospector.Core.Tests.Utilities
{
    public class Verify
    {
        public const int Once = 1;
        public const int Never = 0;

        public static void Some<T>(Option<T> option)
        {
            Assert.True(option.HasValue, "Expected Some but found None");
        }

        public static void None<T>(Option<T> option)
        {
            Assert.False(option.HasValue, "Expected None but found Some");
        }
    }
}