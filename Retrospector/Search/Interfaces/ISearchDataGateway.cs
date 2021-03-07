using System.Collections.Generic;
using Retrospector.Search.Models;

namespace Retrospector.Search.Interfaces
{
    public interface ISearchDataGateway
    {
        IEnumerable<Dictionary<RetrospectorAttribute, string>> Search(QueryTree query);
    }
}