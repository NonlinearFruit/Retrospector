using System.Linq.Expressions;
using Retrospector.Search.Models;
using Filter = System.Func<Retrospector.DataStorage.MediaEntity, Retrospector.DataStorage.ReviewEntity, Retrospector.DataStorage.FactoidEntity, bool>;

namespace Retrospector.Search.Interfaces
{
    public interface ILeafExpressionBuilder
    {
        Expression<Filter> BuildExpression(QueryLeaf leaf);
    }
}