using Optional;
using Retrospector.Core.Search.Models;

namespace Retrospector.Core.Search.Interfaces
{
    public interface ILeafOperator
    {
        Option<QueryLeaf> Parse(string query);
    }
}