using System.Collections.Generic;
using Retrospector.Core.Search.Interfaces;
using Retrospector.Core.Search.Models;

namespace Retrospector.Maui.Search
{
    public class SearchPresenter : ISearchPresenter
    {
        public void Searched(IEnumerable<Dictionary<RetrospectorAttribute, string>> results)
        {
            throw new System.NotImplementedException();
        }
    }
}