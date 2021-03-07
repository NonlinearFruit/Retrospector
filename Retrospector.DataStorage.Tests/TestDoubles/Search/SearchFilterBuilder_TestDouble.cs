using System;
using System.Diagnostics.CodeAnalysis;
using Retrospector.Core.Search.Models;
using Retrospector.DataStorage.Factoids.Entities;
using Retrospector.DataStorage.Medias.Entities;
using Retrospector.DataStorage.Reviews.Entities;
using Retrospector.DataStorage.Search.Interfaces;

namespace Retrospector.DataStorage.Tests.TestDoubles.Search
{
    [SuppressMessage("ReSharper", "InconsistentNaming")]
    public class SearchFilterBuilder_TestDouble : ISearchFilterBuilder
    {
        public Func<MediaEntity, ReviewEntity, FactoidEntity, bool> ReturnFor_BuildExpression { get; set; }
        public QueryTree LastTreePassedTo_BuildExpression { get; set; }
        public int CountOf_BuildExpression_Calls { get; set; }
        public Func<MediaEntity, ReviewEntity, FactoidEntity, bool> BuildFilter(QueryTree tree)
        {
            CountOf_BuildExpression_Calls++;
            LastTreePassedTo_BuildExpression = tree;
            return ReturnFor_BuildExpression;
        }

        public bool ReturnFor_Filter { get; set; }
        public FactoidEntity LastFactoidPassedTo_Filter { get; set; }
        public ReviewEntity LastReviewPassedTo_Filter { get; set; }
        public MediaEntity LastMediaPassedTo_Filter { get; set; }
        public int CountOf_Filter_Calls { get; set; }
        public bool Filter(MediaEntity media, ReviewEntity review, FactoidEntity factoid)
        {
            CountOf_Filter_Calls++;
            LastMediaPassedTo_Filter = media;
            LastReviewPassedTo_Filter = review;
            LastFactoidPassedTo_Filter = factoid;
            return ReturnFor_Filter;
        }
    }
}