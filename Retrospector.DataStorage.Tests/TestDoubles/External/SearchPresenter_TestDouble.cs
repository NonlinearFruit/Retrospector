using System.Collections.Generic;
using System.Diagnostics.CodeAnalysis;
using Retrospector.Core.Search.Interfaces;
using Retrospector.Core.Search.Models;

namespace Retrospector.DataStorage.Tests.TestDoubles.External
{
    [SuppressMessage("ReSharper", "InconsistentNaming")]
    public class SearchPresenter_TestDouble : ISearchPresenter
    {
        public int CountOfCallsTo_Searched { get; set; }
        public IEnumerable<Dictionary<RetrospectorAttribute, string>> LastResultsPassedTo_Searched { get; set; }
        public void Searched(IEnumerable<Dictionary<RetrospectorAttribute, string>> results)
        {
            CountOfCallsTo_Searched++;
            LastResultsPassedTo_Searched = results;
        }
    }
}