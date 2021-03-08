using System;
using System.Collections.Generic;
using System.Diagnostics.CodeAnalysis;
using System.Linq.Expressions;
using Retrospector.DataStorage.Models;
using Retrospector.Search.Interfaces;
using Retrospector.Search.Models;

namespace Retrospector.Tests.TestDoubles.Search
{
    [SuppressMessage("ReSharper", "InconsistentNaming")]
    public class LeafExpressionBuilder_TestDouble : ILeafExpressionBuilder
    {
        public int CountOfCallsTo_BuildExpression { get; set; }
        public QueryLeaf LastLeafPassedTo_BuildExpression { get; set; }
        public Expression<Func<Media, Review, Factoid, bool>> ReturnFor_BuildExpression { get; set; }
        public IList<Expression<Func<Media, Review, Factoid, bool>>> ReturnsFor_BuildExpression { get; set; }
        public Expression<Func<Media, Review, Factoid, bool>> BuildExpression(QueryLeaf leaf)
        {
            CountOfCallsTo_BuildExpression++;
            LastLeafPassedTo_BuildExpression = leaf;
            return ReturnsFor_BuildExpression?[CountOfCallsTo_BuildExpression-1] ?? ReturnFor_BuildExpression;
        }
    }
}