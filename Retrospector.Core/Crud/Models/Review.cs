using System;
using Retrospector.Core.Crud.Interfaces;

namespace Retrospector.Core.Crud.Models
{
    public class Review : IModel
    {
        public DateTime Date{ get; set; }
        public string User{ get; set; }
        public string Content { get; set; }
        public int Rating{ get; set; }
        public int MediaId{ get; set; }
        public int Id{ get; set; }
    }
}