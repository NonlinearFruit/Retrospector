using System.Collections.Generic;
using Retrospector.Core.Search.Models;

namespace Retrospector.Core.Search.Interfaces
{
    public interface IOperator
    {
        OperatorType OperatorType { get; }
        IEnumerable<string> Parse(string query);
    }
}