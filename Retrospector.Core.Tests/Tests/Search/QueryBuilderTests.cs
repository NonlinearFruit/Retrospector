using System;
using System.Linq;
using Optional;
using Retrospector.Core.Search;
using Retrospector.Core.Search.Interfaces;
using Retrospector.Core.Search.Models;
using Retrospector.Core.Tests.TestDoubles.Search;
using Retrospector.Core.Tests.Utilities;
using Xunit;

namespace Retrospector.Core.Tests.Tests.Search
{
    public class QueryBuilderTests
    {
        private IQueryBuilder _builder;
        private LeafOperator_TestDouble _leafOp;
        private Operator_TestDouble[] _operators;

        public QueryBuilderTests()
        {
            _leafOp = new LeafOperator_TestDouble {ReturnFor_Parse = new QueryLeaf().Some()};
            _operators = new[]
            {
                new Operator_TestDouble {ReturnFor_Parse = s => new string[0]},
                new Operator_TestDouble {ReturnFor_Parse = s => new string[0]}
            };
            _builder = new QueryBuilder(_leafOp, _operators);
        }

        [Theory]
        [InlineData(null)]
        [InlineData("")]
        [InlineData("  ")]
        public void gets_everything_when_empty_query(string query)
        {
            var tree = _builder.BuildQuery(query);

            Assert.Equal(OperatorType.GiveMeEverything, tree.Type);
        }

        [Fact]
        public void gets_everything_when_no_operators()
        {
            var query = "find this";
            var builder = new QueryBuilder(_leafOp, new IOperator[0]);

            var tree = builder.BuildQuery(query);

            Assert.Equal(OperatorType.GiveMeEverything, tree.Type);
        }

        [Fact]
        public void calls_multiple_operators()
        {
            var query = "find this";

            _builder.BuildQuery(query);

            foreach (var op in _operators)
            {
                Assert.Equal(Verify.Once, op.CountOf_Parse_Calls);
                Assert.Equal(query, op.LastQueryPassedTo_Parse);
            }
        }

        [Fact]
        public void returns_default_tree_when_no_operators_apply()
        {
            var query = "find this";
            _leafOp.ReturnFor_Parse = Option.None<QueryLeaf>();
            foreach (var op in _operators)
                op.ReturnFor_Parse = s => new string[0];

            var tree = _builder.BuildQuery(query);

            var defaultTree = QueryTreeGenerator.GetDefaultTree(query);
            Assert.Equal(defaultTree.Type, tree.Type);
            Assert.Equal(defaultTree.Branches.Count(), tree.Branches.Count());
            Assert.Equal(defaultTree.Leaves.Count(), tree.Leaves.Count());
        }

        [Fact]
        public void builds_tree_of_proper_depth()
        {
            var depth = 4;
            var query = new string(new char[(int) Math.Pow(2, depth)]);
            _operators[0] = new Operator_SplitsInputExactlyInHalf();

            var tree = _builder.BuildQuery(query);

            VerifyTreeHasDepth(tree, depth);
        }

        [Fact]
        public void builds_query_trees_with_the_type_matching_the_operator()
        {
            var query = "12345678";
            var type = OperatorType.And;
            _operators[0] = new Operator_SplitsInputExactlyInHalf {OperatorType = type};

            var tree = _builder.BuildQuery(query);

            VerifyEveryBranchIsType(tree, type);
        }

        [Fact]
        public void builds_the_proper_number_of_leaves()
        {
            var query = "12345678";
            _operators[0] = new Operator_SplitsInputExactlyInHalf();

            var tree = _builder.BuildQuery(query);

            var count = query.Length;
            Assert.Equal(count, GetLeafCount(tree));
        }

        [Fact]
        public void calls_leaf_operator()
        {
            var query = "find this";

            _builder.BuildQuery(query);

            Assert.Equal(Verify.Once, _leafOp.CountOf_Parse_Calls);
            Assert.Equal(query, _leafOp.LastQueryPassedTo_Parse);
        }

        [Fact]
        public void uses_the_leaf_from_the_leaf_operator()
        {
            var query = "find this";
            var leaf = new QueryLeaf();
            _leafOp.ReturnFor_Parse = leaf.Some();

            var tree = _builder.BuildQuery(query);

            var leafResult = Assert.Single(tree.Leaves);
            Assert.Equal(leaf, leafResult);
        }

        [Fact]
        public void returns_default_tree_when_leaf_operator_fails()
        {
            var query = "12345678";
            _leafOp.ReturnFor_Parse = Option.None<QueryLeaf>();
            _operators[0] = new Operator_SplitsInputExactlyInHalf();

            var tree = _builder.BuildQuery(query);

            var count = query.Length;
            Assert.Equal(count * GetLeafCount(QueryTreeGenerator.GetDefaultTree()), GetLeafCount(tree));
        }

        [Fact]
        public void builds_sub_branches_correctly()
        {
            var query = "123456";
            _operators[0] = new Operator_SplitsInputExactlyInHalf {OperatorType = OperatorType.Or};
            _operators.Last().OperatorType = OperatorType.Not;
            _operators.Last().ReturnFor_Parse = s => s.Length < 1 ? new string[0] : new[] {s.Substring(1)};

            var tree = _builder.BuildQuery(query);

            Assert.Equal(OperatorType.Or, tree.Type);
            Assert.Equal(2, tree.Branches.Count());
            Assert.Empty(tree.Leaves);

            foreach (var branch in tree.Branches)
            {
                Assert.Equal(OperatorType.Not, branch.Type);
                Assert.Single(branch.Branches);
                Assert.Empty(branch.Leaves);
            }
        }

        private static void VerifyTreeHasDepth(QueryTree tree, int depth)
        {
            while (depth > 1)
            {
                Assert.NotEmpty(tree.Branches);
                tree = tree.Branches.First();
                depth -= 1;
            }
            Assert.Empty(tree.Branches);
        }

        private static void VerifyEveryBranchIsType(QueryTree tree, OperatorType type)
        {
            Assert.Equal(type, tree.Type);
            foreach (var branch in tree.Branches)
                VerifyEveryBranchIsType(branch, type);
        }

        private static int GetLeafCount(QueryTree tree)
            => !tree.Branches.Any()
                ? tree.Leaves.Count()
                : tree.Branches.Sum(GetLeafCount);
    }
}