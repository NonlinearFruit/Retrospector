using System;
using System.Diagnostics.CodeAnalysis;
using Retrospector.DataStorage.Models;
using Retrospector.Search.Interfaces;
using Retrospector.Search.Models;

namespace Retrospector.Tests.TestDoubles.Search
{
    [SuppressMessage("ReSharper", "InconsistentNaming")]
    public class SearchFilterBuilder_TestDouble : ISearchFilterBuilder
    {
        public Func<Media, Review, Factoid, bool> ReturnFor_BuildExpression { get; set; }
        public QueryTree LastTreePassedTo_BuildExpression { get; set; }
        public int CountOf_BuildExpression_Calls { get; set; }
        public Func<Media, Review, Factoid, bool> BuildFilter(QueryTree tree)
        {
            CountOf_BuildExpression_Calls++;
            LastTreePassedTo_BuildExpression = tree;
            return ReturnFor_BuildExpression;
        }

        public bool ReturnFor_Filter { get; set; }
        public Factoid LastFactoidPassedTo_Filter { get; set; }
        public Review LastReviewPassedTo_Filter { get; set; }
        public Media LastMediaPassedTo_Filter { get; set; }
        public int CountOf_Filter_Calls { get; set; }
        public bool Filter(Media media, Review review, Factoid factoid)
        {
            CountOf_Filter_Calls++;
            LastMediaPassedTo_Filter = media;
            LastReviewPassedTo_Filter = review;
            LastFactoidPassedTo_Filter = factoid;
            return ReturnFor_Filter;
        }
    }
}