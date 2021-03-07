using System;
using Retrospector.Core.Search.Models;
using Retrospector.DataStorage.Factoids.Entities;
using Retrospector.DataStorage.Medias.Entities;
using Retrospector.DataStorage.Reviews.Entities;

namespace Retrospector.DataStorage.Search.Interfaces
{
    public interface ISearchFilterBuilder
    {
        Func<MediaEntity, ReviewEntity, FactoidEntity, bool> BuildFilter(QueryTree tree);
    }
}