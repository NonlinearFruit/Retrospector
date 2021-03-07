using System.Linq.Expressions;
using Retrospector.Core.Search.Models;
using Filter = System.Func<Retrospector.DataStorage.Medias.Entities.MediaEntity, Retrospector.DataStorage.Reviews.Entities.ReviewEntity, Retrospector.DataStorage.Factoids.Entities.FactoidEntity, bool>;

namespace Retrospector.DataStorage.Search.Interfaces
{
    public interface ILeafExpressionBuilder
    {
        Expression<Filter> BuildExpression(QueryLeaf leaf);
    }
}