using System.Linq;
using Retrospector.Core.Search.Interfaces;
using Retrospector.Core.Search.Models;

namespace Retrospector.Core.Search
{
    public class QueryBuilder : IQueryBuilder
    {
        private readonly ILeafOperator _leafOp;
        private readonly IOperator[] _operators;

        public QueryBuilder(ILeafOperator leafOp, params IOperator[] operators)
        {
            _leafOp = leafOp;
            _operators = operators;
        }

        public QueryTree BuildQuery(string query)
        {
            if (string.IsNullOrWhiteSpace(query) || !_operators.Any())
                return new QueryTree {Type = OperatorType.GiveMeEverything};
            foreach (var op in _operators)
            {
                var list = op.Parse(query).ToList();
                if (!list.Any())
                    continue;
                var root = new QueryTree {Type = op.OperatorType};
                foreach (var subQuery in list)
                    AddBranches(root, subQuery);
                return root;
            }

            return _leafOp.Parse(query)
                .Match(
                    leaf => new QueryTree {Type = OperatorType.And, Leaves = new[] {leaf}},
                    () => GetDefaultLeaves(query)
                );
        }

        private void AddBranches(QueryTree root, string query)
        {
            foreach (var op in _operators)
            {
                var list = op.Parse(query).ToList();
                if (!list.Any())
                    continue;
                var branch = new QueryTree {Type = op.OperatorType};
                root.Branches.Add(branch);
                foreach (var subQuery in list)
                    AddBranches(branch, subQuery);
                return;
            }

            AddLeaf(root, query);
        }

        private void AddLeaf(QueryTree root, string query)
        {
            var leaf = _leafOp.Parse(query);
            leaf.MatchNone(() => root.Branches.Add(GetDefaultLeaves(query)));
            leaf.MatchSome(l => root.Leaves.Add(l));
        }

        private static QueryTree GetDefaultLeaves(string query)
        {
            var defaultTree = new QueryTree {Type = OperatorType.Or};
            defaultTree.Leaves.Add(new QueryLeaf
            {
                Attribute = RetrospectorAttribute.MediaTitle,
                Comparator = Comparator.Contains,
                SearchValue = query
            });
            defaultTree.Leaves.Add(new QueryLeaf
            {
                Attribute = RetrospectorAttribute.MediaCreator,
                Comparator = Comparator.Contains,
                SearchValue = query
            });
            defaultTree.Leaves.Add(new QueryLeaf
            {
                Attribute = RetrospectorAttribute.MediaSeason,
                Comparator = Comparator.Contains,
                SearchValue = query
            });
            defaultTree.Leaves.Add(new QueryLeaf
            {
                Attribute = RetrospectorAttribute.MediaEpisode,
                Comparator = Comparator.Contains,
                SearchValue = query
            });
            defaultTree.Leaves.Add(new QueryLeaf
            {
                Attribute = RetrospectorAttribute.MediaCategory,
                Comparator = Comparator.Contains,
                SearchValue = query
            });
            return defaultTree;
        }
    }
}