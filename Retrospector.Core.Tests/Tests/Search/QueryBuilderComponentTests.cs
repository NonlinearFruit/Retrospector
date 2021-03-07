using System.Collections.Generic;
using System.Linq;
using Retrospector.Core.Search;
using Retrospector.Core.Search.Interfaces;
using Retrospector.Core.Search.Models;
using Retrospector.Core.Tests.Utilities;
using Xunit;

namespace Retrospector.Core.Tests.Tests.Search
{
    public class QueryBuilderComponentTests
    {
        private IQueryBuilder _builder;

        public QueryBuilderComponentTests()
        {
            var commandDecoder = new Dictionary<string, RetrospectorAttribute>
            {
                {"T", RetrospectorAttribute.MediaTitle},
                {"C", RetrospectorAttribute.MediaCreator},
                {"S", RetrospectorAttribute.MediaSeason},
                {"E", RetrospectorAttribute.MediaEpisode},
                {"A", RetrospectorAttribute.MediaCategory},
                {"P", RetrospectorAttribute.MediaDescription},
                {"#", RetrospectorAttribute.ReviewRating},
                {"U", RetrospectorAttribute.ReviewUser},
                {"D", RetrospectorAttribute.ReviewDate},
                {"R", RetrospectorAttribute.ReviewContent},
                {"I", RetrospectorAttribute.FactoidTitle},
                {"O", RetrospectorAttribute.FactoidContent}
            };
            var comparatorDecoder = new Dictionary<string, Comparator>
            {
                {"=", Comparator.Equal},
                {">", Comparator.GreaterThan},
                {"<", Comparator.LessThan},
                {"~", Comparator.Contains},
            };
            _builder = new QueryBuilder(
                new LeafOperator("`", commandDecoder, comparatorDecoder),
                new BinaryOperator(OperatorType.And, ":"),
                new BinaryOperator(OperatorType.Or, "|"),
                new UnaryOperator(OperatorType.Not, "!")
            );
        }

        [Fact]
        public void building_leaves_work()
        {
            var query = "`U~ben";

            var tree = _builder.BuildQuery(query);

            var leaf = Assert.Single(tree.Leaves);
            Assert.Equal(RetrospectorAttribute.ReviewUser, leaf.Attribute);
            Assert.Equal(Comparator.Contains, leaf.Comparator);
            Assert.Equal("ben", leaf.SearchValue);
        }

        [Fact]
        public void creating_default_tree_works()
        {
            var query = "find this";
            var defaultTree = QueryTreeGenerator.GetDefaultTree(query);

            var tree = _builder.BuildQuery(query);

            Assert.Equal(defaultTree.Type, tree.Type);
            Assert.Equal(defaultTree.Branches.Count(), tree.Branches.Count());
            Assert.Equal(defaultTree.Leaves.Count(), tree.Leaves.Count());
        }

        [Fact]
        public void and_operator_works()
        {
            var query = "a:b:c";

            var tree = _builder.BuildQuery(query);

            Assert.Equal(OperatorType.And, tree.Type);
            Assert.Equal(3, tree.Branches.Count());
        }

        [Fact]
        public void or_operator_works()
        {
            var query = "a|b|c";

            var tree = _builder.BuildQuery(query);

            Assert.Equal(OperatorType.Or, tree.Type);
            Assert.Equal(3, tree.Branches.Count());
        }

        [Fact]
        public void not_operator_works()
        {
            var query = "!a";

            var tree = _builder.BuildQuery(query);

            Assert.Equal(OperatorType.Not, tree.Type);
            Assert.Single(tree.Branches);
        }

        [Fact]
        public void complicated_query_works()
        {
            var query = "`U=Ben:`T~Sherlock|`S~Sherlock:!`C=BBC";

            var tree = _builder.BuildQuery(query);

            Assert.Equal(OperatorType.And, tree.Type);
            Assert.Equal(2, tree.Branches.Count());
            Assert.Single(tree.Leaves);

            var leaf = tree.Leaves.First();
            Assert.Equal(RetrospectorAttribute.ReviewUser, leaf.Attribute);
            Assert.Equal(Comparator.Equal, leaf.Comparator);
            Assert.Equal("Ben", leaf.SearchValue);

            var orBranch = tree.Branches.First();
            Assert.Equal(OperatorType.Or, orBranch.Type);
            Assert.Empty(orBranch.Branches);
            Assert.Equal(2, orBranch.Leaves.Count());

            var titleLeaf = orBranch.Leaves.First();
            Assert.Equal(RetrospectorAttribute.MediaTitle, titleLeaf.Attribute);
            Assert.Equal(Comparator.Contains, titleLeaf.Comparator);
            Assert.Equal("Sherlock", titleLeaf.SearchValue);

            var seasonLeaf = orBranch.Leaves.Last();
            Assert.Equal(RetrospectorAttribute.MediaSeason, seasonLeaf.Attribute);
            Assert.Equal(Comparator.Contains, seasonLeaf.Comparator);
            Assert.Equal("Sherlock", seasonLeaf.SearchValue);

            var notBranch = tree.Branches.Last();
            Assert.Equal(OperatorType.Not, notBranch.Type);
            Assert.Empty(notBranch.Branches);
            Assert.Single(notBranch.Leaves);

            var notLeaf = notBranch.Leaves.First();
            Assert.Equal(RetrospectorAttribute.MediaCreator, notLeaf.Attribute);
            Assert.Equal(Comparator.Equal, notLeaf.Comparator);
            Assert.Equal("BBC", notLeaf.SearchValue);
        }
    }
}