using Optional;
using Retrospector.Search.Models;

namespace Retrospector.Search.Interfaces
{
    public interface ILeafOperator
    {
        Option<QueryLeaf> Parse(string query);
    }
}