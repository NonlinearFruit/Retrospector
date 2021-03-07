using System.Linq;
using Xunit;

namespace Retrospector.DataStorage.Tests.Utilities
{
    public class CombinatorTests
    {
        [Fact]
        public void returns_empty_when_given_nothing()
        {
            var combinations = Combinator.AllCombinationsOf();

            Assert.Empty(combinations);
        }

        [Fact]
        public void returns_list_when_single_list()
        {
            var list = new[] {new object[] {"hello"}};

            var combinations = Combinator.AllCombinationsOf(list);

            Assert.Equal(list, combinations);
        }

        [Fact]
        public void combines_two_lists()
        {
            var listA = new[] {new object[] {"hello"}, new object[]{"goodbye"} };
            var listB = new[] {new object[] {"greetings"}, new object[]{"farewell"}};

            var combinations = Combinator.AllCombinationsOf(listA, listB);

            var expectedSize = listA.Length * listB.Length;
            Assert.Equal(expectedSize, combinations.Count());
        }

        [Fact]
        public void combines_three_lists()
        {
            var listA = new[] {new object[] {"hello"}, new object[]{"goodbye"} };
            var listB = new[] {new object[] {"greetings"}, new object[]{"farewell"}};
            var listC = new[] {new object[] {"howdy"}, new object[]{"so long"}};

            var combinations = Combinator.AllCombinationsOf(listA, listB, listC);

            var expectedSize = listA.Length * listB.Length * listC.Length;
            Assert.Equal(expectedSize, combinations.Count());
        }

        [Fact]
        public void objects_in_first_list_stay_first()
        {
            var listA = new[] {new object[] {"hello"}, new object[]{"goodbye"} };
            var listB = new[] {new object[] {"greetings"}, new object[]{"farewell"}};

            var combinations = Combinator.AllCombinationsOf(listA, listB);

            foreach (var combination in combinations)
                Assert.Contains(combination.First(), listA.SelectMany(o => o));
        }

        [Fact]
        public void objects_in_last_list_stay_last()
        {
            var listA = new[] {new object[] {"hello"}, new object[]{"goodbye"} };
            var listB = new[] {new object[] {"greetings"}, new object[]{"farewell"}};

            var combinations = Combinator.AllCombinationsOf(listA, listB);

            foreach (var combination in combinations)
                Assert.Contains(combination.Last(), listB.SelectMany(o => o));
        }
    }
}