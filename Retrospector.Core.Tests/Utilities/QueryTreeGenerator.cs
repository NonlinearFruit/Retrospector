using Retrospector.Core.Search.Models;

namespace Retrospector.Core.Tests.Utilities
{
    public static class QueryTreeGenerator
    {
        public static QueryTree GetDefaultTree(string query = "")
        {
            var defaultTree = new QueryTree {Type = OperatorType.Or};
            defaultTree.Leaves.Add(new QueryLeaf
            {
                Attribute = RetrospectorAttribute.MediaTitle, Comparator = Comparator.Contains, SearchValue = query
            });
            defaultTree.Leaves.Add(new QueryLeaf
            {
                Attribute = RetrospectorAttribute.MediaCreator, Comparator = Comparator.Contains, SearchValue = query
            });
            defaultTree.Leaves.Add(new QueryLeaf
            {
                Attribute = RetrospectorAttribute.MediaSeason, Comparator = Comparator.Contains, SearchValue = query
            });
            defaultTree.Leaves.Add(new QueryLeaf
            {
                Attribute = RetrospectorAttribute.MediaEpisode, Comparator = Comparator.Contains, SearchValue = query
            });
            defaultTree.Leaves.Add(new QueryLeaf
            {
                Attribute = RetrospectorAttribute.MediaCategory, Comparator = Comparator.Contains, SearchValue = query
            });
            return defaultTree;
        }
    }
}