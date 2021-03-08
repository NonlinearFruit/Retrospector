using System;
using Retrospector.Search.Models;
using Retrospector.DataStorage.Models;

namespace Retrospector.Search.Interfaces
{
    public interface ISearchFilterBuilder
    {
        Func<Media, Review, Factoid, bool> BuildFilter(QueryTree tree);
    }
}