using Retrospector.Core.Search.Models;

namespace Retrospector.Core.Search.Interfaces
{
    public interface IQueryBuilder
    {
        QueryTree BuildQuery(string query);
    }
}