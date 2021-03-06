using System;
using System.Linq;
using System.Linq.Expressions;
using Retrospector.Search.Models;
using Retrospector.DataStorage.Models;
using Retrospector.Search.Interfaces;
using Filter = System.Func<Retrospector.DataStorage.Models.Media, Retrospector.DataStorage.Models.Review, Retrospector.DataStorage.Models.Factoid, bool>;

namespace Retrospector.Search
{
    public class SearchFilterBuilder : ISearchFilterBuilder
    {
        private readonly ILeafExpressionBuilder _leafBuilder;
        private ParameterExpression _mediaParameter = Expression.Parameter(typeof(Media), "media");
        private ParameterExpression _reviewParameter = Expression.Parameter(typeof(Review), "review");
        private ParameterExpression _factoidParameter = Expression.Parameter(typeof(Factoid), "factoid");
        private static Filter MatchEverything = (m, r, f) => true;
        private static Expression<Filter> MatchEverything2 = (m, r, f) => true;

        public SearchFilterBuilder(ILeafExpressionBuilder leafBuilder)
        {
            _leafBuilder = leafBuilder;
        }

        public Filter BuildFilter(QueryTree tree)
        {
            if (tree == null || tree.Type == OperatorType.GiveMeEverything)
                return MatchEverything;
            var expression = ParseTree(tree);
            return Compile(expression);
        }

        private Filter Compile(Expression expression)
        {
            return Expression.Lambda<Filter>(
                expression,
                _mediaParameter, _reviewParameter, _factoidParameter
            ).Compile();
        }

        private InvocationExpression Invoke(Expression<Filter> func)
        {
            return Expression.Invoke(func, _mediaParameter, _reviewParameter, _factoidParameter);
        }

        private Expression ParseTree(QueryTree tree)
        {
            if (tree.Type == OperatorType.GiveMeEverything)
                return MatchEverything2;
            var expressions = tree
                .Branches
                .Select(ParseTree)
                .Concat(tree.Leaves.Select(l => (Expression) Invoke(_leafBuilder.BuildExpression(l))))
                .DefaultIfEmpty(Invoke(MatchEverything2));
            return tree.Type switch
            {
                OperatorType.Not => Expression.Not(expressions.First()),
                OperatorType.And => expressions.Aggregate(Expression.And),
                OperatorType.Or => expressions.Aggregate(Expression.Or),
                OperatorType.GiveMeEverything => throw new ArgumentOutOfRangeException(),
                _ => throw new ArgumentOutOfRangeException()
            };
        }
    }
}