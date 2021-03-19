using System.Collections.Generic;
using System.Diagnostics.CodeAnalysis;
using Retrospector.Search.Interfaces;
using Retrospector.Search.Models;

namespace Retrospector.Tests.TestDoubles.Search
{
    [SuppressMessage("ReSharper", "InconsistentNaming")]
    public class SearchDataGateway_TestDouble : ISearchDataGateway
    {
        public int CountOfCallsTo_Search { get; set; }
        public QueryTree LastQueryPassedTo_Search { get; set; }
        public IEnumerable<Dictionary<RetrospectorAttribute, string>> ReturnFor_Search { get; set; }
        public IEnumerable<Dictionary<RetrospectorAttribute, string>> Search(QueryTree query)
        {
            CountOfCallsTo_Search++;
            LastQueryPassedTo_Search = query;
            return ReturnFor_Search;
        }
    }
}