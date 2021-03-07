using System.Collections.Generic;
using System.Linq;

namespace Retrospector.Tests.Utilities
{
    public static class Combinator
    {
        public static IEnumerable<object[]> AllCombinationsOf(params IEnumerable<object[]>[] sets) =>
            sets.Length < 1
                ? new List<object[]>()
                : sets.Skip(1).Aggregate(sets[0], AddExtraSet);

        private static List<object[]> AddExtraSet(IEnumerable<object[]> combinations, IEnumerable<object[]> set) =>
            set
                .SelectMany(value => combinations, Join)
                .ToList();

        private static object[] Join(object[] first, object[] last) =>
            last
                .Concat(first)
                .ToArray();
    }
}