using System.Linq.Expressions;
using Retrospector.Search.Models;
using Filter = System.Func<Retrospector.DataStorage.Models.Media, Retrospector.DataStorage.Models.Review, Retrospector.DataStorage.Models.Factoid, bool>;

namespace Retrospector.Search.Interfaces
{
    public interface ILeafExpressionBuilder
    {
        Expression<Filter> BuildExpression(QueryLeaf leaf);
    }
}