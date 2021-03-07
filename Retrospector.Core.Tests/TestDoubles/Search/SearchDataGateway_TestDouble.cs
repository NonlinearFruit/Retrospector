using System.Collections.Generic;
using Retrospector.Core.Search.Interfaces;
using Retrospector.Core.Search.Models;

namespace Retrospector.Core.Tests.TestDoubles.Search
{
    public class SearchDataGateway_TestDouble : ISearchDataGateway
    {
        public int CountOf_Search_Calls { get; set; }
        public QueryTree LastQueryPassedTo_Search { get; set; }
        public IEnumerable<Dictionary<RetrospectorAttribute, string>> ReturnFor_Search { get; set; }
        public IEnumerable<Dictionary<RetrospectorAttribute, string>> Search(QueryTree query)
        {
            CountOf_Search_Calls++;
            LastQueryPassedTo_Search = query;
            return ReturnFor_Search;
        }
    }
}