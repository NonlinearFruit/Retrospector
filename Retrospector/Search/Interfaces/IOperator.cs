using System.Collections.Generic;
using Retrospector.Search.Models;

namespace Retrospector.Search.Interfaces
{
    public interface IOperator
    {
        OperatorType OperatorType { get; }
        IEnumerable<string> Parse(string query);
    }
}