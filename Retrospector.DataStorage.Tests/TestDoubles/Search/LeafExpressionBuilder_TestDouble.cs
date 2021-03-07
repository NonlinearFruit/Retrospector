using System;
using System.Collections.Generic;
using System.Diagnostics.CodeAnalysis;
using System.Linq.Expressions;
using Retrospector.Core.Search.Models;
using Retrospector.DataStorage.Factoids.Entities;
using Retrospector.DataStorage.Medias.Entities;
using Retrospector.DataStorage.Reviews.Entities;
using Retrospector.DataStorage.Search.Interfaces;

namespace Retrospector.DataStorage.Tests.TestDoubles.Search
{
    [SuppressMessage("ReSharper", "InconsistentNaming")]
    public class LeafExpressionBuilder_TestDouble : ILeafExpressionBuilder
    {
        public int CountOfCallsTo_BuildExpression { get; set; }
        public QueryLeaf LastLeafPassedTo_BuildExpression { get; set; }
        public Expression<Func<MediaEntity, ReviewEntity, FactoidEntity, bool>> ReturnFor_BuildExpression { get; set; }
        public IList<Expression<Func<MediaEntity, ReviewEntity, FactoidEntity, bool>>> ReturnsFor_BuildExpression { get; set; }
        public Expression<Func<MediaEntity, ReviewEntity, FactoidEntity, bool>> BuildExpression(QueryLeaf leaf)
        {
            CountOfCallsTo_BuildExpression++;
            LastLeafPassedTo_BuildExpression = leaf;
            return ReturnsFor_BuildExpression?[CountOfCallsTo_BuildExpression-1] ?? ReturnFor_BuildExpression;
        }
    }
}