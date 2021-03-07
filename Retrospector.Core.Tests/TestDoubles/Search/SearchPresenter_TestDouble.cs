using System.Collections.Generic;
using Retrospector.Core.Search.Interfaces;
using Retrospector.Core.Search.Models;

namespace Retrospector.Core.Tests.TestDoubles.Search
{
    public class SearchPresenter_TestDouble : ISearchPresenter
    {
        public IEnumerable<Dictionary<RetrospectorAttribute, string>> LastResultsPassedTo_Search { get; set; }
        public int CountOf_Searched_Calls { get; set; }
        public void Searched(IEnumerable<Dictionary<RetrospectorAttribute, string>> results)
        {
            CountOf_Searched_Calls++;
            LastResultsPassedTo_Search = results;
        }
    }
}