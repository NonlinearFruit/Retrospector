using System;
using Retrospector.Search.Models;
using Retrospector.DataStorage;

namespace Retrospector.Search.Interfaces
{
    public interface ISearchFilterBuilder
    {
        Func<MediaEntity, ReviewEntity, FactoidEntity, bool> BuildFilter(QueryTree tree);
    }
}