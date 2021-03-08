using System.Linq;
using System.Linq.Expressions;
using Retrospector.DataStorage.Models;
using Retrospector.Search;
using Retrospector.Search.Interfaces;
using Retrospector.Search.Models;
using Retrospector.Tests.TestDoubles.Search;
using Xunit;
using Filter = System.Func<Retrospector.DataStorage.Models.Media, Retrospector.DataStorage.Models.Review, Retrospector.DataStorage.Models.Factoid, bool>;

namespace Retrospector.Tests.Tests.Search
{
    public class SearchFilterBuilderTests
    {
        private ISearchFilterBuilder _builder;
        private LeafExpressionBuilder_TestDouble _leafBuilder;

        public SearchFilterBuilderTests()
        {
            _leafBuilder = new LeafExpressionBuilder_TestDouble
            {
                ReturnFor_BuildExpression = (m, r, f) => true
            };
            _builder = new SearchFilterBuilder(_leafBuilder);
        }

        [Theory]
        [InlineData(0)]
        [InlineData(1)]
        [InlineData(10)]
        public void calls_leaf_builder_for_each_leaf_in_root_node(int countOfLeaves)
        {
            var query = ArrangeQuery(countOfLeaves: countOfLeaves);

            _builder.BuildFilter(query);

            Assert.Equal(countOfLeaves, _leafBuilder.CountOfCallsTo_BuildExpression);
        }

        [Theory]
        [InlineData(true)]
        [InlineData(false)]
        public void uses_leaf_expression(bool expectedResult)
        {
            var query = ArrangeQuery(countOfLeaves: 1);
            _leafBuilder.ReturnFor_BuildExpression = (m, r, f) => expectedResult;

            var function = _builder.BuildFilter(query);
            var actualResult = function.Invoke(null, null, null);

            Assert.Equal(expectedResult, actualResult);
        }

        [Fact]
        public void matches_everything_when_tree_is_null()
        {
            var function = _builder.BuildFilter(null);

            Assert.NotNull(function);
            Assert.True(function.Invoke(null, null, null));
        }

        [Fact]
        public void returns_valid_delegate()
        {
            var function = _builder.BuildFilter(new QueryTree());
            function.Invoke(new Media(), new Review(), new Factoid());
        }

        [Fact]
        public void delegate_always_returns_true_when_set_to_match_everything()
        {
            var query = ArrangeQuery(OperatorType.GiveMeEverything);

            var function = _builder.BuildFilter(query);
            var isMatch = function.Invoke(new Media(), new Review(), new Factoid());

            Assert.True(isMatch);
        }

        [Theory]
        [InlineData(true, true)]
        [InlineData(true, true, true)]
        [InlineData(true, true, true, true)]
        [InlineData(false, false)]
        [InlineData(false, false, true)]
        [InlineData(false, true, false, true)]
        [InlineData(false, false, false, false)]
        public void and_operator_performs_and_on_leaf_expressions(bool shouldMatch, params bool[] leafResults)
        {
            var query = ArrangeQuery(OperatorType.And, leafResults.Length);
            _leafBuilder.ReturnsFor_BuildExpression = leafResults
                .Select<bool, Expression<Filter>>(b => (m, r, f) => b)
                .ToList();

            var function = _builder.BuildFilter(query);
            var isMatch = function.Invoke(new Media(), new Review(), new Factoid());

            Assert.Equal(shouldMatch, isMatch);
        }

        [Theory]
        [InlineData(true, true)]
        [InlineData(true, true, true)]
        [InlineData(true, true, true, true)]
        [InlineData(false, false)]
        [InlineData(true, false, true)]
        [InlineData(true, true, false, true)]
        [InlineData(false, false, false, false)]
        public void or_operator_performs_or_on_leaf_expressions(bool shouldMatch, params bool[] leafResults)
        {
            var query = ArrangeQuery(OperatorType.Or, leafResults.Length);
            _leafBuilder.ReturnsFor_BuildExpression = leafResults
                .Select<bool, Expression<Filter>>(b => (m, r, f) => b)
                .ToList();

            var function = _builder.BuildFilter(query);
            var isMatch = function.Invoke(new Media(), new Review(), new Factoid());

            Assert.Equal(shouldMatch, isMatch);
        }

        [Theory]
        [InlineData(true, false)]
        [InlineData(false, true)]
        public void not_operator_performs_not_on_leaf_expression(bool shouldMatch, bool leafResult)
        {
            var query = ArrangeQuery(OperatorType.Not, 1);
            _leafBuilder.ReturnFor_BuildExpression = (m, r, f) => leafResult;

            var function = _builder.BuildFilter(query);
            var isMatch = function.Invoke(new Media(), new Review(), new Factoid());

            Assert.Equal(shouldMatch, isMatch);
        }

        private static QueryTree ArrangeQuery(OperatorType type = OperatorType.And, int countOfLeaves = 1)
        {
            return new QueryTree
            {
                Type = type,
                Leaves = Enumerable
                    .Repeat(new QueryLeaf(), countOfLeaves)
                    .ToList()
            };
        }
    }
}