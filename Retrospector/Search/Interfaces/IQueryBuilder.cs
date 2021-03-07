using Retrospector.Search.Models;

namespace Retrospector.Search.Interfaces
{
    public interface IQueryBuilder
    {
        QueryTree BuildQuery(string query);
    }
}