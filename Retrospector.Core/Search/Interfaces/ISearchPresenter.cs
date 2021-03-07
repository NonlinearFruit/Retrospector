using System.Collections.Generic;
using Retrospector.Core.Search.Models;

namespace Retrospector.Core.Search.Interfaces
{
    public interface ISearchPresenter
    {
        void Searched(IEnumerable<Dictionary<RetrospectorAttribute, string>> results);
    }
}